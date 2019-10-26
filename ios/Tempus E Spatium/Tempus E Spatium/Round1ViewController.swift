//
//  Round1ViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 6/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import SwiftIcons
import Alamofire
import SwiftyJSON
import SVGKit
import OGVKit

class Round1ViewController: UIViewController {
    
    @IBOutlet weak var mPlayer1Container: UIView!
    
    var mSecs:CGFloat = 0, mMaxTime:CGFloat = 0
    var mCooldownSecs:CGFloat = 5
    
    var mPointsA = 0, mPointsB = 0
    
    var mArticles:[String:String]?
    
    var mPlayer1:ExteriorViewController?
    var mPlayer2:ExteriorViewController?
    var mTimer:Timer?
    var mCoolDownTimer:Timer?
    
    var mQuestionType:Int? = 5
    
    private let mPreferences = UserDefaults.standard
    
    @IBAction func unwindToRound1ViewController(segue: UIStoryboardSegue) {
    }
    
    @objc func updateCounter() {
        mPlayer1!.mDonutTime!.progress = mSecs / mMaxTime
        mPlayer2!.mDonutTime!.progress = mSecs / mMaxTime
        mPlayer1!.mDonutTime!.progressLabel.text = String(Int(mSecs))
        mPlayer2!.mDonutTime!.progressLabel.text = String(Int(mSecs))
        mSecs -= 1
        if mSecs < 0 {
            stopCounterAndReveal()
        }
    }
    
    @objc func coolDown() {
        mSecs -= 1
        if mSecs < 0 {
            mCoolDownTimer!.invalidate()
            mCoolDownTimer = nil
            replaceFragment()
        }
    }
    
    @objc func stopCounterAndReveal() {
        mTimer!.invalidate()
        mTimer = nil
        reveal()
        mCooldownSecs = 5
        mCoolDownTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(coolDown), userInfo: nil, repeats: true)
    }
    
    func reveal() {
        //        mPlayer1!.mDonutTime!.progress = 0
        //        mPlayer2!.mDonutTime!.progress = 0
        //        mPlayer1!.mDonutTime!.progressLabel.text = "0"
        //        mPlayer2!.mDonutTime!.progressLabel.text = "0"
        switch mQuestionType! {
        case 0:
            let controller = mPlayer1!.children.first! as! BlanksGameViewController
            let controller2 = mPlayer2!.children.first! as! BlanksGameViewController
            //            controller1.reveal()
            //            controller2.reveal()
            break
        case 1:
            let controller = mPlayer1!.children.first! as! DateGameViewController
            let controller2 = mPlayer2!.children.first! as! DateGameViewController
            controller.reveal()
            controller2.reveal()
            break
        case 2:
            let controller = mPlayer1!.children.first! as! FlagGameViewController
            let controller2 = mPlayer2!.children.first! as! FlagGameViewController
            //            controller1.reveal()
            //            controller2.reveal()
            break
        case 3:
            let controller = mPlayer1!.children.first! as! MapGameViewController
            let controller2 = mPlayer2!.children.first! as! MapGameViewController
            
            if let player = controller.mOgvPlayerView {
                player.pause()
                controller.mOgvPlayerView = nil
            }
            controller.reveal()
            controller2.reveal()
            break
        case 4:
            let controller = mPlayer1!.children.first! as! MapGameViewController
            let controller2 = mPlayer2!.children.first! as! MapGameViewController
            if let player = controller.mOgvPlayerView {
                player.pause()
                controller.mOgvPlayerView = nil
            }
            controller.reveal()
            controller2.reveal()
            break
        default:
            break
        }
    }
    
    
    func showLoadingDialog() {
        let alert = UIAlertController(title: nil, message: NSLocalizedString("loading", comment: ""), preferredStyle: .alert)
        
        let loadingIndicator = UIActivityIndicatorView(frame: CGRect(x: 10, y: 5, width: 50, height: 50))
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.style = .medium
        loadingIndicator.startAnimating()
        
        alert.view.addSubview(loadingIndicator)
        present(alert, animated: false)
    }
    
    func closeLoadingDialog() {
        dismiss(animated: false)
    }
    
    func replaceFragment() {
        // if marks >= 380 then go to EndgameViewController return
        if mPointsA >= 380 {
            if let view1 = mPlayer1!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view1.removeFromSuperview()
                let controller = storyboard!.instantiateViewController(withIdentifier: "EndgameViewController") as! EndgameViewController
                controller.view.frame = mPlayer1!.mFragmentContainer.bounds
                controller.willMove(toParent: self)
                mPlayer1!.mFragmentContainer.addSubview(controller.view)
                mPlayer1!.addChild(controller)
                closeLoadingDialog()
                controller.didMove(toParent: self)
                controller.view.backgroundColor = mPlayer1!.view.backgroundColor
                let bluetoothGif = UIImage.gifImageWithName("player_1_wins")
                let imageView = UIImageView(image: bluetoothGif)
                imageView.frame = controller.mAnimation.bounds
                controller.mAnimation.addSubview(imageView)
            }
            if let view2 = mPlayer2!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view2.removeFromSuperview()
                let controller = storyboard!.instantiateViewController(withIdentifier: "EndgameViewController") as! EndgameViewController
                controller.view.frame = mPlayer2!.mFragmentContainer.bounds
                controller.willMove(toParent: self)
                mPlayer2!.mFragmentContainer.addSubview(controller.view)
                mPlayer2!.addChild(controller)
                closeLoadingDialog()
                controller.didMove(toParent: self)
                controller.view.backgroundColor = mPlayer2!.view.backgroundColor
                let bluetoothGif = UIImage.gifImageWithName("well_done")
                let imageView = UIImageView(image: bluetoothGif)
                imageView.frame = controller.mAnimation.bounds
                controller.mAnimation.addSubview(imageView)
            }
            performSegue(withIdentifier: "toHighscoreInputDialog", sender: nil)
            return
        } else if mPointsB >= 380 {
            if let view1 = mPlayer1!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view1.removeFromSuperview()
                let controller = storyboard!.instantiateViewController(withIdentifier: "EndgameViewController") as! EndgameViewController
                controller.view.frame = mPlayer1!.mFragmentContainer.bounds
                controller.willMove(toParent: self)
                mPlayer1!.mFragmentContainer.addSubview(controller.view)
                mPlayer1!.addChild(controller)
                closeLoadingDialog()
                controller.didMove(toParent: self)
                controller.view.backgroundColor = mPlayer1!.view.backgroundColor
                let bluetoothGif = UIImage.gifImageWithName("well_done")
                let imageView = UIImageView(image: bluetoothGif)
                imageView.frame = controller.mAnimation.bounds
                controller.mAnimation.addSubview(imageView)
            }
            if let view2 = mPlayer2!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view2.removeFromSuperview()
                let controller = storyboard!.instantiateViewController(withIdentifier: "EndgameViewController") as! EndgameViewController
                controller.view.frame = mPlayer2!.mFragmentContainer.bounds
                controller.willMove(toParent: self)
                mPlayer2!.mFragmentContainer.addSubview(controller.view)
                mPlayer2!.addChild(controller)
                closeLoadingDialog()
                controller.didMove(toParent: self)
                controller.view.backgroundColor = mPlayer2!.view.backgroundColor
                let bluetoothGif = UIImage.gifImageWithName("player_2_wins")
                let imageView = UIImageView(image: bluetoothGif)
                imageView.frame = controller.mAnimation.bounds
                controller.mAnimation.addSubview(imageView)
            }
            performSegue(withIdentifier: "toHighscoreInputDialog", sender: nil)
            return
        }
        
        var x:Int = (0...4).randomElement()!
        while x == mQuestionType {
            x = (0...4).randomElement()!
        }
        mQuestionType = x
        switch mQuestionType {
        case 0:
            if let view1 = mPlayer1!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view1.removeFromSuperview()
                mPlayer1!.children.first!.removeFromParent()
                let controller = storyboard!.instantiateViewController(withIdentifier: "BlanksGameViewController") as! BlanksGameViewController
                controller.view.frame = mPlayer1!.mFragmentContainer.bounds
                controller.willMove(toParent: self)
                mPlayer1!.mFragmentContainer.addSubview(controller.view)
                mPlayer1!.addChild(controller)
                closeLoadingDialog()
                controller.didMove(toParent: self)
                controller.view.backgroundColor = mPlayer1!.view.backgroundColor
            }
            if let view2 = mPlayer2!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view2.removeFromSuperview()
                mPlayer2!.children.first!.removeFromParent()
                let controller = storyboard!.instantiateViewController(withIdentifier: "BlanksGameViewController") as! BlanksGameViewController
                controller.view.frame = mPlayer2!.mFragmentContainer.bounds
                controller.willMove(toParent: self)
                mPlayer2!.mFragmentContainer.addSubview(controller.view)
                mPlayer2!.addChild(controller)
                closeLoadingDialog()
                controller.didMove(toParent: self)
                controller.view.backgroundColor = mPlayer2!.view.backgroundColor
            }
            mMaxTime = 6
            mSecs = 6
            mTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(updateCounter), userInfo: nil, repeats: true)
        case 1:
            showLoadingDialog()
            if let view1 = mPlayer1!.mFragmentContainer.subviews.first {
                view1.removeFromSuperview()
            }
            mPlayer1!.children.first!.removeFromParent()
            let controller = storyboard!.instantiateViewController(withIdentifier: "DateGameViewController") as! DateGameViewController
            controller.view.frame = mPlayer1!.mFragmentContainer.bounds
            controller.view.backgroundColor = mPlayer1!.view.backgroundColor
            
            if let view2 = mPlayer2!.mFragmentContainer.subviews.first {
                view2.removeFromSuperview()
            }
            mPlayer2!.children.first!.removeFromParent()
            let controller2 = storyboard!.instantiateViewController(withIdentifier: "DateGameViewController") as! DateGameViewController
            controller2.view.frame = mPlayer2!.mFragmentContainer.bounds
            controller2.view.backgroundColor = mPlayer2!.view.backgroundColor
            
            
            let savedLocale = mPreferences.string(forKey: "PREF_LOCALE")!
            let sparql =
            """
            #defaultView:Timeline
            SELECT DISTINCT ?eventLabel ?dateLabel ?coordLabel ?img
            WHERE
            {
            { ?event wdt:P31* wd:Q178561. }
            ?event wdt:P585+ ?date.
            ?event wdt:P18+ ?img.
            OPTIONAL { ?event wdt:P625 ?coord }
            FILTER(YEAR(?date) > 1900).
            SERVICE wikibase:label { bd:serviceParam wikibase:language "\(savedLocale)" }
            FILTER(EXISTS {
            ?event rdfs:label ?lang_label.
            FILTER(LANG(?lang_label) = "\(savedLocale)")
            })
            }
            ORDER BY DESC(?dateLabel)
            """
            print("https://query.wikidata.org/sparql?format=json&query=\(sparql.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!)")
            AF.request("https://query.wikidata.org/sparql?format=json&query=\(sparql.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!)")
                .validate(statusCode: 200..<300)
                //            .validate(contentType: ["application/json"])
                .responseData { response in
                    switch response.result {
                    case .success(let value):
                        let json = JSON(value)
                        
                        let chosen = json["results"]["bindings"].arrayValue.randomElement()!
                        print("qswdef")
                        print(chosen)
                        print(chosen["eventLabel"]["value"].stringValue)
                        controller.mEvent = chosen["eventLabel"]["value"].stringValue
                        controller2.mEvent = chosen["eventLabel"]["value"].stringValue
                        
                        let date = chosen["dateLabel"]["value"].stringValue
                        let year = Int(date.prefix(4))!
                        let month = Int(date[date.index(date.startIndex, offsetBy: 5)...date.index(date.startIndex, offsetBy: 6)])!
                        let lowerBound = year - (0...50).randomElement()!
                        let upperBound = year + 50
                        
                        controller.mYear = year
                        controller2.mYear = year
                        controller.mMonth = month
                        controller2.mMonth = month
                        controller.mLowerBoundYear = lowerBound
                        controller2.mLowerBoundYear = lowerBound
                        controller.mUpperBoundYear = upperBound
                        controller2.mUpperBoundYear = upperBound
                        
                        let url = URL(string: chosen["img"]["value"].stringValue)!
                        let data = try! Data(contentsOf: url)
                        controller.mHintPic.image = UIImage(data: data)
                        controller2.mHintPic.image = UIImage(data: data)
                        
                        controller.mSubmitBtn.addTarget(nil, action: #selector(self.stopCounterAndReveal), for: .touchDown)
                        controller2.mSubmitBtn.addTarget(nil, action: #selector(self.stopCounterAndReveal), for: .touchDown)
                        
                        controller.willMove(toParent: self)
                        self.mPlayer1!.mFragmentContainer.addSubview(controller.view)
                        self.mPlayer1!.addChild(controller)
                        controller.didMove(toParent: self)
                        
                        controller2.willMove(toParent: self)
                        self.mPlayer2!.mFragmentContainer.addSubview(controller2.view)
                        self.mPlayer2!.addChild(controller2)
                        controller2.didMove(toParent: self)
                        
                        self.mMaxTime = 10
                        self.mSecs = 10
                        self.mTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(self.updateCounter), userInfo: nil, repeats: true)
                        
                        self.closeLoadingDialog()
                    case .failure(let error):
                        print("asdfasdf\(error.errorDescription!)")
                        print("asdfasdf\(error)")
                        
                    }
            }
        case 2:
            showLoadingDialog()
            if let view1 = mPlayer1!.mFragmentContainer.subviews.first {
                view1.removeFromSuperview()
            }
            mPlayer1!.children.first!.removeFromParent()
            let controller = storyboard!.instantiateViewController(withIdentifier: "FlagGameViewController") as! FlagGameViewController
            controller.view.frame = mPlayer1!.mFragmentContainer.bounds
            controller.view.backgroundColor = mPlayer1!.view.backgroundColor
            
            
            if let view2 = mPlayer2!.mFragmentContainer.subviews.first {
                view2.removeFromSuperview()
            }
            mPlayer2!.children.first!.removeFromParent()
            let controller2 = storyboard!.instantiateViewController(withIdentifier: "FlagGameViewController") as! FlagGameViewController
            controller2.view.frame = mPlayer2!.mFragmentContainer.bounds
            controller2.view.backgroundColor = mPlayer2!.view.backgroundColor
            
            
            
            let savedLocale = mPreferences.string(forKey: "PREF_LOCALE")!
            let sparql = """
            SELECT DISTINCT ?countryLabel ?capitalLabel ?flagLabel ?armsLabel ?imgLabel ?population
            ?country_local ?capital_local
            WHERE
            {
            ?country wdt:P31 wd:Q3624078.
            FILTER NOT EXISTS {?country wdt:P31 wd:Q3024240}
            FILTER NOT EXISTS {?country wdt:P31 wd:Q28171280}
            ?country wdt:P36 ?capital.
            ?country wdt:P41 ?flag.
            ?country wdt:P94 ?arms.
            ?capital wdt:P18 ?img.
            ?country wdt:P1082 ?population.
            SERVICE wikibase:label { bd:serviceParam wikibase:language "en" }
            SERVICE wikibase:label { bd:serviceParam wikibase:language "\(savedLocale)".
            ?country rdfs:label ?country_local.
            } hint:Prior hint:runLast false.
            SERVICE wikibase:label { bd:serviceParam wikibase:language "\(savedLocale)".
            ?capital rdfs:label ?capital_local.
            } hint:Prior hint:runLast false.
            }
            ORDER BY ?countryLabel
            """
            print("https://query.wikidata.org/sparql?format=json&query=\(sparql.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!)")
            AF.request("https://query.wikidata.org/sparql?format=json&query=\(sparql.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!)")
                .validate(statusCode: 200..<300)
                //            .validate(contentType: ["application/json"])
                .responseData { response in
                    switch response.result {
                    case .success(let value):
                        let json = JSON(value)
                        
                        let list = json["results"]["bindings"].arrayValue
                        var randNumbers:[Int] = [(0..<list.count).randomElement()!]
                        
                        var tmp = (0..<list.count).randomElement()!
                        while randNumbers.contains(tmp) {
                            tmp = (0..<list.count).randomElement()!
                        }
                        randNumbers.append(tmp)
                        
                        tmp = (0..<list.count).randomElement()!
                        while randNumbers.contains(tmp) {
                            tmp = (0..<list.count).randomElement()!
                        }
                        randNumbers.append(tmp)
                        
                        tmp = (0..<list.count).randomElement()!
                        while randNumbers.contains(tmp) {
                            tmp = (0..<list.count).randomElement()!
                        }
                        randNumbers.append(tmp)
                        
                        let choosen = list[randNumbers[(0...3).randomElement()!]]
                        
                        //TODO: How to reveal.
                        
                        controller.willMove(toParent: self)
                        self.mPlayer1!.mFragmentContainer.addSubview(controller.view)
                        self.mPlayer1!.addChild(controller)
                        controller.didMove(toParent: self)
                        
                        controller2.willMove(toParent: self)
                        self.mPlayer2!.mFragmentContainer.addSubview(controller2.view)
                        self.self.mPlayer2!.addChild(controller2)
                        controller2.didMove(toParent: self)
                        
                        let svg1:SVGKImage = SVGKImage(contentsOf: URL(string: list[0]["armsLabel"]["value"].stringValue)!)
                        let fast = SVGKFastImageView(svgkImage: svg1)!
                        let aspect = fast.frame.width / fast.frame.height
                        fast.contentMode = .scaleAspectFill
                        fast.frame = CGRect(x: 0, y: 0, width: 90 * aspect, height: 90)
                        //            print(controller.mFlagA.frame)
                        controller.mFlagA.contentMode = .scaleAspectFill
                        controller.mFlagA.addSubview(fast)
                        
                        controller2.mFlagA.contentMode = .scaleAspectFill
                        controller2.mFlagA.addSubview(fast)
                        
                        let svg2:SVGKImage = SVGKImage(contentsOf: URL(string: list[1]["armsLabel"]["value"].stringValue)!)
                        let fast2 = SVGKFastImageView(svgkImage: svg2)!
                        let aspect2 = fast2.frame.width / fast2.frame.height
                        fast2.contentMode = .scaleAspectFill
                        fast2.frame = CGRect(x: 0, y: 0, width: 90 * aspect2, height: 90)
                        //            print(controller.mFlagA.frame)
                        controller.mFlagB.contentMode = .scaleAspectFill
                        controller.mFlagB.addSubview(fast2)
                        
                        controller2.mFlagB.contentMode = .scaleAspectFill
                        controller2.mFlagB.addSubview(fast2)
                        
                        let svg3:SVGKImage = SVGKImage(contentsOf: URL(string: list[2]["armsLabel"]["value"].stringValue)!)
                        let fast3 = SVGKFastImageView(svgkImage: svg3)!
                        let aspect3 = fast3.frame.width / fast3.frame.height
                        fast3.contentMode = .scaleAspectFill
                        fast3.frame = CGRect(x: 0, y: 0, width: 90 * aspect3, height: 90)
                        //            print(controller.mFlagA.frame)
                        controller.mFlagC.contentMode = .scaleAspectFill
                        controller.mFlagC.addSubview(fast3)
                        
                        controller2.mFlagC.contentMode = .scaleAspectFill
                        controller2.mFlagC.addSubview(fast3)
                        
                        let svg4:SVGKImage = SVGKImage(contentsOf: URL(string: list[3]["armsLabel"]["value"].stringValue)!)
                        let fast4 = SVGKFastImageView(svgkImage: svg4)!
                        let aspect4 = fast4.frame.width / fast4.frame.height
                        fast4.contentMode = .scaleAspectFill
                        fast4.frame = CGRect(x: 0, y: 0, width: 90 * aspect4, height: 90)
                        //            print(controller.mFlagA.frame)
                        controller.mFlagD.contentMode = .scaleAspectFill
                        controller.mFlagD.addSubview(fast4)
                        
                        controller2.mFlagD.contentMode = .scaleAspectFill
                        controller2.mFlagD.addSubview(fast4)
                        
                        //                        print(choosen["countryLabel"]["value"].stringValue)
                        //
                        //
                        //                        for x in randNumbers {
                        //                            print(list[x]["countryLabel"]["value"].stringValue)
                        //                            print(list[x]["country_local"]["value"].stringValue)
                        //                            print(list[x]["anthemLabel"]["value"].stringValue)
                        //                            print(list[x]["anthem_local"]["value"].stringValue)
                        //                            print(list[x]["audioLabel"]["value"].stringValue)
                        //                        }
                        
                        self.mMaxTime = 10
                        self.mSecs = 10
                        self.mTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(self.updateCounter), userInfo: nil, repeats: true)
                        
                        self.closeLoadingDialog()
                    case .failure(let error):
                        print("asdfasdf\(error.errorDescription!)")
                        print("asdfasdf\(error)")
                        
                    }
            }
        case 3:
            showLoadingDialog()
            if let view1 = mPlayer1!.mFragmentContainer.subviews.first {
                view1.removeFromSuperview()
            }
            mPlayer1!.children.first!.removeFromParent()
            if let view2 = mPlayer2!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view2.removeFromSuperview()
            }
            mPlayer2!.children.first!.removeFromParent()
            
            let controller = storyboard!.instantiateViewController(withIdentifier: "MapGameViewController") as! MapGameViewController
            controller.view.frame = mPlayer1!.mFragmentContainer.bounds
            controller.view.backgroundColor = mPlayer1!.view.backgroundColor
            controller.willMove(toParent: self)
            
            
            let controller2 = storyboard!.instantiateViewController(withIdentifier: "MapGameViewController") as! MapGameViewController
            controller2.view.frame = mPlayer2!.mFragmentContainer.bounds
            controller2.view.backgroundColor = mPlayer2!.view.backgroundColor
            
            let savedLocale = mPreferences.string(forKey: "PREF_LOCALE")!
            let sparql = """
            SELECT DISTINCT ?countryLabel ?country_local ?anthemLabel ?anthem_local ?audioLabel
            WHERE
            {
            ?country wdt:P31 wd:Q3624078 .
            FILTER NOT EXISTS {?country wdt:P31 wd:Q3024240}
            FILTER NOT EXISTS {?country wdt:P31 wd:Q28171280}
            ?country wdt:P85 ?anthem.
            ?anthem wdt:P51 ?audio.
            SERVICE wikibase:label { bd:serviceParam wikibase:language "en".
            }
            SERVICE wikibase:label { bd:serviceParam wikibase:language "\(savedLocale)".
            ?country rdfs:label ?country_local.
            } hint:Prior hint:runLast false.
            SERVICE wikibase:label { bd:serviceParam wikibase:language "\(savedLocale)".
            ?anthem rdfs:label ?anthem_local.
            } hint:Prior hint:runLast false.
            }
            ORDER BY ?countryLabel
            """
            print("https://query.wikidata.org/sparql?format=json&query=\(sparql.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!)")
            AF.request("https://query.wikidata.org/sparql?format=json&query=\(sparql.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!)")
                .validate(statusCode: 200..<300)
                //            .validate(contentType: ["application/json"])
                .responseData { response in
                    switch response.result {
                    case .success(let value):
                        let json = JSON(value)
                        
                        let chosen = json["results"]["bindings"].arrayValue.randomElement()!
                        
                        controller.mOgvPlayerView = OGVPlayerView()
                        controller.mOgvPlayerView!.frame = .null
                        controller.view.addSubview(controller.mOgvPlayerView!)
                        controller.mOgvPlayerView!.sourceURL = URL(string: chosen["audioLabel"]["value"].stringValue)!
                        controller.mOgvPlayerView!.play()
                        
                        controller.mCorrectAnswerEn = chosen["countryLabel"]["value"].stringValue
                        controller2.mCorrectAnswerEn = chosen["countryLabel"]["value"].stringValue
                        controller.mCorrectAnswer = chosen["country_local"]["value"].stringValue
                        controller2.mCorrectAnswer = chosen["country_local"]["value"].stringValue
                        
                        controller.mSubmitBtn.addTarget(nil, action: #selector(self.stopCounterAndReveal), for: .touchDown)
                        controller2.mSubmitBtn.addTarget(nil, action: #selector(self.stopCounterAndReveal), for: .touchDown)
                        
                        self.mPlayer1!.mFragmentContainer.addSubview(controller.view)
                        self.mPlayer1!.addChild(controller)
                        self.closeLoadingDialog()
                        controller.didMove(toParent: self)
                        
                        self.mPlayer2!.mFragmentContainer.addSubview(controller2.view)
                        self.mPlayer2!.addChild(controller2)
                        self.closeLoadingDialog()
                        controller2.didMove(toParent: self)
                        
                        self.closeLoadingDialog()
                        
                        self.mMaxTime = 10
                        self.mSecs = 10
                        self.mTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(self.updateCounter), userInfo: nil, repeats: true)
                        
                        //                let list = json["results"]["bindings"].arrayValue
                        //                var randNumbers:[Int] = [(0..<list.count).randomElement()!]
                        //
                        //                var tmp = (0..<list.count).randomElement()!
                        //                while randNumbers.contains(tmp) {
                        //                    tmp = (0..<list.count).randomElement()!
                        //                }
                        //                randNumbers.append(tmp)
                        //
                        //                tmp = (0..<list.count).randomElement()!
                        //                while randNumbers.contains(tmp) {
                        //                    tmp = (0..<list.count).randomElement()!
                        //                }
                        //                randNumbers.append(tmp)
                        //
                        //                tmp = (0..<list.count).randomElement()!
                        //                while randNumbers.contains(tmp) {
                        //                    tmp = (0..<list.count).randomElement()!
                        //                }
                        //                randNumbers.append(tmp)
                        //
                        //                let choosen = list[randNumbers[(0...3).randomElement()!]]
                        //                print(choosen["countryLabel"]["value"].stringValue)
                        //
                        //
                        //                for x in randNumbers {
                        //                    print(list[x]["countryLabel"]["value"].stringValue)
                        //                    print(list[x]["country_local"]["value"].stringValue)
                        //                    print(list[x]["anthemLabel"]["value"].stringValue)
                        //                    print(list[x]["anthem_local"]["value"].stringValue)
                        //                    print(list[x]["audioLabel"]["value"].stringValue)
                    //                }
                    case .failure(let error):
                        print("asdfasdf\(error.errorDescription!)")
                        print("asdfasdf\(error)")
                        
                    }
            }
        case 4:
            showLoadingDialog()
            if let view1 = mPlayer1!.mFragmentContainer.subviews.first {
                view1.removeFromSuperview()
            }
            mPlayer1!.children.first!.removeFromParent()
            if let view2 = mPlayer2!.mFragmentContainer.subviews.first {
                showLoadingDialog()
                view2.removeFromSuperview()
            }
            mPlayer2!.children.first!.removeFromParent()
            
            let controller = storyboard!.instantiateViewController(withIdentifier: "MapGameViewController") as! MapGameViewController
            controller.view.frame = mPlayer1!.mFragmentContainer.bounds
            controller.view.backgroundColor = mPlayer1!.view.backgroundColor
            controller.willMove(toParent: self)
            
            
            let controller2 = storyboard!.instantiateViewController(withIdentifier: "MapGameViewController") as! MapGameViewController
            controller2.view.frame = mPlayer2!.mFragmentContainer.bounds
            controller2.view.backgroundColor = mPlayer2!.view.backgroundColor
            
            let savedLocale = mPreferences.string(forKey: "PREF_LOCALE")!
            let sparql = """
            SELECT DISTINCT ?countryLabel ?country_local ?anthemLabel ?anthem_local ?audioLabel
            WHERE
            {
            ?country wdt:P31 wd:Q3624078 .
            FILTER NOT EXISTS {?country wdt:P31 wd:Q3024240}
            FILTER NOT EXISTS {?country wdt:P31 wd:Q28171280}
            ?country wdt:P85 ?anthem.
            ?anthem wdt:P51 ?audio.
            SERVICE wikibase:label { bd:serviceParam wikibase:language "en".
            }
            SERVICE wikibase:label { bd:serviceParam wikibase:language "\(savedLocale)".
            ?country rdfs:label ?country_local.
            } hint:Prior hint:runLast false.
            SERVICE wikibase:label { bd:serviceParam wikibase:language "\(savedLocale)".
            ?anthem rdfs:label ?anthem_local.
            } hint:Prior hint:runLast false.
            }
            ORDER BY ?countryLabel
            """
            print("https://query.wikidata.org/sparql?format=json&query=\(sparql.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!)")
            AF.request("https://query.wikidata.org/sparql?format=json&query=\(sparql.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!)")
                .validate(statusCode: 200..<300)
                //            .validate(contentType: ["application/json"])
                .responseData { response in
                    switch response.result {
                    case .success(let value):
                        let json = JSON(value)
                        
                        let chosen = json["results"]["bindings"].arrayValue.randomElement()!
                        
                        let mOgvPlayerView = OGVPlayerView()
                        mOgvPlayerView.frame = .null
                        controller.view.addSubview(mOgvPlayerView)
                        mOgvPlayerView.sourceURL = URL(string: chosen["audioLabel"]["value"].stringValue)!
                        mOgvPlayerView.play()
                        
                        controller.mCorrectAnswerEn = chosen["countryLabel"]["value"].stringValue
                        controller2.mCorrectAnswerEn = chosen["countryLabel"]["value"].stringValue
                        controller.mCorrectAnswer = chosen["country_local"]["value"].stringValue
                        controller2.mCorrectAnswer = chosen["country_local"]["value"].stringValue
                        
                        controller.mSubmitBtn.addTarget(nil, action: #selector(self.stopCounterAndReveal), for: .touchDown)
                        controller2.mSubmitBtn.addTarget(nil, action: #selector(self.stopCounterAndReveal), for: .touchDown)
                        
                        self.mPlayer1!.mFragmentContainer.addSubview(controller.view)
                        self.mPlayer1!.addChild(controller)
                        self.closeLoadingDialog()
                        controller.didMove(toParent: self)
                        
                        self.mPlayer2!.mFragmentContainer.addSubview(controller2.view)
                        self.mPlayer2!.addChild(controller2)
                        self.closeLoadingDialog()
                        controller2.didMove(toParent: self)
                        
                        self.closeLoadingDialog()
                        
                        self.mMaxTime = 10
                        self.mSecs = 10
                        self.mTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(self.updateCounter), userInfo: nil, repeats: true)
                        
                        //                let list = json["results"]["bindings"].arrayValue
                        //                var randNumbers:[Int] = [(0..<list.count).randomElement()!]
                        //
                        //                var tmp = (0..<list.count).randomElement()!
                        //                while randNumbers.contains(tmp) {
                        //                    tmp = (0..<list.count).randomElement()!
                        //                }
                        //                randNumbers.append(tmp)
                        //
                        //                tmp = (0..<list.count).randomElement()!
                        //                while randNumbers.contains(tmp) {
                        //                    tmp = (0..<list.count).randomElement()!
                        //                }
                        //                randNumbers.append(tmp)
                        //
                        //                tmp = (0..<list.count).randomElement()!
                        //                while randNumbers.contains(tmp) {
                        //                    tmp = (0..<list.count).randomElement()!
                        //                }
                        //                randNumbers.append(tmp)
                        //
                        //                let choosen = list[randNumbers[(0...3).randomElement()!]]
                        //                print(choosen["countryLabel"]["value"].stringValue)
                        //
                        //
                        //                for x in randNumbers {
                        //                    print(list[x]["countryLabel"]["value"].stringValue)
                        //                    print(list[x]["country_local"]["value"].stringValue)
                        //                    print(list[x]["anthemLabel"]["value"].stringValue)
                        //                    print(list[x]["anthem_local"]["value"].stringValue)
                        //                    print(list[x]["audioLabel"]["value"].stringValue)
                    //                }
                    case .failure(let error):
                        print("asdfasdf\(error.errorDescription!)")
                        print("asdfasdf\(error)")
                        
                    }
            }
        default:
            break
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mPlayer1Container.transform =  CGAffineTransform(rotationAngle: .pi)
        
        mPlayer1 = (children.first! as! ExteriorViewController)
        mPlayer2 = (children.last! as! ExteriorViewController)
        mPlayer1!.view.backgroundColor = UIColor(named: "CosmicLatte")!
        
        mMaxTime = 10
        mSecs = 10
        mTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(updateCounter), userInfo: nil, repeats: true)
        
        mPlayer1!.mScoreChangeLbl!.text = "+0"
        mPlayer1!.mScoreChangeLbl!.textColor = UIColor(named: "AndroidGreen")!
        
        mPlayer2!.mScoreChangeLbl!.text = "-10"
        mPlayer2!.mScoreChangeLbl!.textColor = UIColor(named: "Firebrick")!
    }
}
