//
//  FlagGameViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 15/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit

class FlagGameViewController: UIViewController {

    @IBOutlet weak var mQuestion: UILabel!
    

    @IBOutlet weak var mClickAreaA: UIView!
    @IBOutlet weak var mFlagA: UIImageView!
    @IBOutlet weak var mIndicatorA: UILabel!
    
    @IBOutlet weak var mClickAreaB: UIView!
    @IBOutlet weak var mFlagB: UIImageView!
    @IBOutlet weak var mIndicatorB: UILabel!
    
    @IBOutlet weak var mClickAreaC: UIView!
    @IBOutlet weak var mFlagC: UIImageView!
    @IBOutlet weak var mIndicatorC: UILabel!
    
    @IBOutlet weak var mClickAreaD: UIView!
    @IBOutlet weak var mFlagD: UIImageView!
    @IBOutlet weak var mIndicatorD: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
